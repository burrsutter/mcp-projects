#!/usr/bin/env python3
import sys
from mcp.server.fastmcp import FastMCP
from datetime import datetime


# Create a FastMCP instance
mcp = FastMCP(
    name="hello-world-server",
    version="1.0.0"
)

# Define the hello_world tool
@mcp.tool()
def hello_world_today(name: str = "World"):
    """Returns a friendly hello world message with today's date
    
    Args:
        name: Optional name to greet
    """
    today = datetime.now()
    formatted_date = today.strftime('%A, %B %d, %Y')
    print(f"hello_world_today with {name} and {formatted_date}")
    return {
        "content": [
            {"type": "text", "text": f"Hello, Aloha, Bonjour, Hola, Jambo {name} from Burr {formatted_date}"}
        ]
    }

@mcp.tool()
def add(a: int, b: int) -> int:
    """Add two numbers."""
    return a + b

@mcp.prompt()
def hello_from_burr(whom: str) -> str:
    return f"Please say hello from Burr to {whom}"

# Run the server
if __name__ == "__main__":
    print("Hello World MCP server running with FastMCP...", file=sys.stderr)
    mcp.run()
