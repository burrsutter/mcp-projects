import asyncio
from typing import Optional
from contextlib import AsyncExitStack

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client
from openai import OpenAI
import os
import logging
import json
from dotenv import load_dotenv

load_dotenv()  # load environment variables from .env

API_KEY = os.getenv("API_KEY")
MODEL_NAME=os.getenv("MODEL_NAME")
INFERENCE_SERVER_URL=os.getenv("INFERENCE_SERVER_URL")

# Set up logging configuration
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
)
logger = logging.getLogger(__name__)
logging.getLogger("openai").setLevel(logging.WARNING)
logging.getLogger("httpx").setLevel(logging.WARNING)


class MCPClient:
    def __init__(self, debug=True):
        # Initialize session and client objects
        self.session: Optional[ClientSession] = None
        self.exit_stack = AsyncExitStack()
        self.debug = debug
        
        self.client = OpenAI(
            api_key=API_KEY,
            base_url=INFERENCE_SERVER_URL
        )

    async def connect_to_server(self, server_script_path: str):
        """Connect to an MCP server
        
        Args:
            server_script_path: Path to the server script (.py or .js)
        """
        is_python = server_script_path.endswith('.py')
        is_js = server_script_path.endswith('.js')
        if not (is_python or is_js):
            raise ValueError("Server script must be a .py or .js file")
            
        command = "python" if is_python else "node"
        server_params = StdioServerParameters(
            command=command,
            args=[server_script_path],
            env=None
        )
        
        stdio_transport = await self.exit_stack.enter_async_context(stdio_client(server_params))
        self.stdio, self.write = stdio_transport
        self.session = await self.exit_stack.enter_async_context(ClientSession(self.stdio, self.write))
        
        await self.session.initialize()
        
        # List available tools
        response = await self.session.list_tools()
        tools = response.tools
        print("\nConnected to server with tools:", [tool.name for tool in tools])

    async def process_query(self, query: str) -> str:
        """Process a query using LLM and available tools"""
        messages = [
            {
                "role": "user",
                "content": query
            }
        ]

        response = await self.session.list_tools()

        # Anthropic
        # available_tools = [{ 
        #     "name": tool.name,
        #     "description": tool.description,
        #     "input_schema": tool.inputSchema
        # } for tool in response.tools]

        # OpenAI
        available_tools = [{ 
            "type": "function",
            "function": {
                "name": tool.name,
                "description": tool.description,
                "parameters": tool.inputSchema
            }
        } for tool in response.tools]

        if self.debug:
            logger.info(available_tools)

        # Initial LLM call
        response = self.client.chat.completions.create(
            model=MODEL_NAME,
            max_tokens=1000,
            messages=messages,
            tools=available_tools,
            tool_choice="auto",
            temperature=0.1
        )

        # Process response and handle tool calls
        assistant_message = response.choices[0].message
        initial_response = assistant_message.content or ""

        if self.debug:
            logger.info(f"assistant_message: {assistant_message}")
            logger.info(f"intial_response: {initial_response}")

        tool_results = []
        final_text = []

        if assistant_message.tool_calls:
            if self.debug:
                logger.info(f"Tool calls requested: {len(assistant_message.tool_calls)}")

            messages.append({
                "role": "assistant",
                "content": assistant_message.content,
                "tool_calls": assistant_message.tool_calls
            })                
            
            # Loop through all tool calls
            for tool_call in assistant_message.tool_calls:
                tool_name = tool_call.function.name
                tool_args = json.loads(tool_call.function.arguments)
                a_tool_result = await self.session.call_tool(tool_name, tool_args)
                tool_content = a_tool_result.content if hasattr(a_tool_result, 'content') else str(a_tool_result)
                tool_results.append({"call": tool_name, "result": tool_content[0].text})
                final_text.append(f"[Calling tool {tool_name} with args {tool_args}]")

                if self.debug:
                    logger.info(f"a_tool_result type: {type(a_tool_result)}")
                    logger.info(f"a_tool_result: {a_tool_result}")

                # Add the tool result to the conversation
                messages.append({
                        "role": "tool",
                        "tool_call_id": tool_call.id,
                        "content": tool_content[0].text
                })
        # if assistant_message.tool_calls:

        if self.debug:
            logger.info(f"messages: {messages}")

        # Back to the LLM with any tool responses included
        second_response = self.client.chat.completions.create(
            model=MODEL_NAME,
            messages=messages
        )                

        second_response_content = second_response.choices[0].message.content or ""
        final_text.append("\n" + second_response_content)

        return "\n".join(final_text)

    async def chat_loop(self):
        """Run an interactive chat loop"""
        print("\nMCP Client Started!")
        print("Type your queries or 'quit' to exit.")
        
        while True:
            try:
                query = input("\nYou: ").strip()
                
                if query.lower() == 'quit':
                    break
                    
                response = await self.process_query(query)
                print("\n" + response)
                    
            except Exception as e:
                print(f"\nError: {str(e)}")
    
    async def cleanup(self):
        """Clean up resources"""
        await self.exit_stack.aclose()

async def main():
    if len(sys.argv) < 2:
        print("Usage: python client.py <path_to_server_script>")
        sys.exit(1)
        
    client = MCPClient()
    try:
        await client.connect_to_server(sys.argv[1])
        await client.chat_loop()
    finally:
        await client.cleanup()

if __name__ == "__main__":
    import sys
    asyncio.run(main())