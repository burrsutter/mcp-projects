# TMDB MCP Server

A Model Context Protocol (MCP) server for The Movie Database (TMDB) API. This server provides tools and resources for interacting with the TMDB API through the MCP protocol.

## Prerequisites

- Node.js (v12 or higher)
- TMDB API Key (get one from [TMDB](https://www.themoviedb.org/settings/api))

## Installation

1. Clone this repository
2. Install dependencies:
   ```
   npm install
   ```

   Note: This server includes the `abort-controller` polyfill for compatibility with older Node.js versions.
3. Create a `.env` file in the root directory with your TMDB API key:
   ```
   TMDB_API_KEY=your_tmdb_api_key_here
   ```

## Usage

### Running the Server

```
npm start
```

Or in development mode with auto-restart on file changes:

```
npm run dev
```

### Adding to MCP Settings

To use this server with Claude or other MCP-compatible systems, add it to your MCP settings configuration file:

#### For Claude Desktop App (macOS)

Edit `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "tmdb": {
      "command": "node",
      "args": ["/Users/burr/ai-projects/mcp-projects/mcp-server-node-tmdb/index.js"],
      "env": {
        "TMDB_API_KEY": "api-key-goes-here"
      },
      "disabled": false,
      "autoApprove": []
    }
  }
}
```

#### For Claude Dev VSCode Extension

Edit the MCP settings file in VSCode:

```json
{
  "mcpServers": {
    "tmdb": {
      "command": "node",
      "args": ["/path/to/tmdb-mcp-server/index.js"],
      "env": {
        "TMDB_API_KEY": "your_tmdb_api_key_here"
      },
      "disabled": false,
      "autoApprove": []
    }
  }
}
```

## Available Tools

The server provides the following tools:

### search_movies

Search for movies by title.

**Parameters:**
- `query` (required): Movie title to search for
- `page` (optional): Page number for results (1-1000)

**Example:**
```json
{
  "query": "Inception",
  "page": 1
}
```

### search_tv

Search for TV shows by title.

**Parameters:**
- `query` (required): TV show title to search for
- `page` (optional): Page number for results (1-1000)

**Example:**
```json
{
  "query": "Breaking Bad",
  "page": 1
}
```

### search_people

Search for people by name.

**Parameters:**
- `query` (required): Person name to search for
- `page` (optional): Page number for results (1-1000)

**Example:**
```json
{
  "query": "Tom Hanks",
  "page": 1
}
```

### get_movie_recommendations

Get movie recommendations based on a movie ID.

**Parameters:**
- `movie_id` (required): TMDB movie ID
- `page` (optional): Page number for results (1-1000)

**Example:**
```json
{
  "movie_id": 550,
  "page": 1
}
```

### get_trending

Get trending movies, TV shows, or people.

**Parameters:**
- `media_type` (required): Media type to get trending items for (`all`, `movie`, `tv`, or `person`)
- `time_window` (required): Time window for trending items (`day` or `week`)

**Example:**
```json
{
  "media_type": "movie",
  "time_window": "week"
}
```

## Available Resources

The server provides the following resources:

### Static Resources

- `tmdb://movie/popular`: List of currently popular movies
- `tmdb://tv/popular`: List of currently popular TV shows

### Resource Templates

- `tmdb://movie/{id}`: Movie details by ID
- `tmdb://tv/{id}`: TV show details by ID
- `tmdb://person/{id}`: Person details by ID

## License

ISC
