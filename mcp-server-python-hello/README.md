# Python Hello World MCP Server

A simple Model Context Protocol (MCP) server implemented in Python using FastMCP that provides greeting tools and prompts.

## Features

- Uses FastMCP for easy MCP server implementation
- Provides a `hello_world_today` tool that returns a greeting message with the current date
- Includes an `add` tool for adding two numbers
- Includes a `hello_from_burr` prompt
- Can be customized with parameters

## Prerequisites

- Python 3.13
- uv package manager (`brew install uv` or `curl -LsSf https://astral.sh/uv/install.sh | sh`)

## Setup

Create a virtual environment and install dependencies:

```bash
uv venv
source .venv/bin/activate
uv add "mcp[cli]" httpx
```

```bash
python --version
```

```
Python 3.13.2
```

## Usage

### Running the Server Locally

For testing purposes, you can run the server directly:

```bash
uv run server.py
```

### Adding to Claude Desktop MCP Settings

Create a `claude_desktop_config.json`

```
/Users/burr/Library/Application Support/Claude/claude_desktop_config.json
```

```json
{
  "mcpServers": {
     "hello-world": {
         "command": "uv",
         "args": [
             "--directory",
             "/Users/burr/ai-projects/mcp-projects/mcp-server-python-hello",
             "run",
             "server.py"
         ],
         "disabled": false,
         "autoApprove": []
    }
  }
}
```

### Using the Tools and Prompts

Once connected, you can use the available tools and prompts:

```
# Using the hello_world_today tool
hello_world_today()  // Returns a greeting with today's date
hello_world_today(name: "Alice")  // Returns a personalized greeting with today's date

# Using the add tool
add(a: 5, b: 3)  // Returns 8

# Using the hello_from_burr prompt
hello_from_burr(whom: "Claude")  // Returns "Please say hello from Burr to Claude"
```

## How It Works

This server uses FastMCP, a Python library that simplifies the implementation of MCP servers:

1. Creates a FastMCP instance with server metadata
2. Defines tools using the `@mcp.tool()` decorator
3. Defines prompts using the `@mcp.prompt()` decorator
4. FastMCP handles all the MCP protocol details (JSON-RPC, stdin/stdout communication)

