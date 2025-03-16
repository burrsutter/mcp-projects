# MCP Client and Servers

## MCP Client

```bash
cd mcp-client-python-llm
```

Review that readme.md

Note: jbang with Java server requires pre-compilation

## MCP Server: Python Hello

https://github.com/modelcontextprotocol/python-sdk


`mcp-server-python-hello` - should work "out of the box" assuming a decent version of Python on the machine


## MCP Server: Node TMDB

https://github.com/modelcontextprotocol/typescript-sdk


`mcp-server-node-tmdb`

Uses https://www.themoviedb.org/ API which requires an API key within the client's .env

## MCP Server: Java Weather

https://quarkus.io/blog/introducing-mcp-servers/


`mcp-server-java-weather`

Uses https://api.weather.gov which has NO API key requirement

`mvn clean compile install` to load into your local .m2/repository

`brew install jbang`

SDKMan as a way to manage Java, mvn, etc. 

https://sdkman.io/


