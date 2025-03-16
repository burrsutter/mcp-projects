
# OpenAI API MCP Client STDIO

Tools Only

No support for MCP Resources nor MCP Prompts

## Servers as .py or .js

mcp-client-default.py supports `python` or `node` executable servers via STDIO

Run the client and identify the path to the .py 

On Mac, I use venv

```bash
python3.11 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

.py MCP Server

```bash
python mcp-client-default.py /Users/burr/ai-projects/mcp-projects/mcp-server-python-hello/server.py
```

.js MCP Server

```bash
python mcp-client-default.py /Users/burr/ai-projects/mcp-projects/mcp-server-node-tmdb/index.js 
```

## jbang

Java MCP Server

```bash
python mcp-client-jbang.py
```




The Anthropic API specific MCP Client in Python

https://raw.githubusercontent.com/modelcontextprotocol/quickstart-resources/refs/heads/main/mcp-client-python/client.py




