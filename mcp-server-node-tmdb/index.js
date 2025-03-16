#!/usr/bin/env node
// Add AbortController polyfill for older Node.js versions
import AbortController from 'abort-controller';
global.AbortController = AbortController;

import { Server } from '@modelcontextprotocol/sdk/server/index.js';
import { StdioServerTransport } from '@modelcontextprotocol/sdk/server/stdio.js';
import {
  CallToolRequestSchema,
  ErrorCode,
  ListResourcesRequestSchema,
  ListResourceTemplatesRequestSchema,
  ListToolsRequestSchema,
  McpError,
  ReadResourceRequestSchema,
} from '@modelcontextprotocol/sdk/types.js';
import axios from 'axios';
import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

const TMDB_API_KEY = process.env.TMDB_API_KEY;
if (!TMDB_API_KEY) {
  throw new Error('TMDB_API_KEY environment variable is required');
}

class TMDBServer {
  constructor() {
    this.server = new Server(
      {
        name: 'tmdb-server',
        version: '1.0.0',
      },
      {
        capabilities: {
          resources: {},
          tools: {},
        },
      }
    );

    this.axiosInstance = axios.create({
      baseURL: 'https://api.themoviedb.org/3',
      params: {
        api_key: TMDB_API_KEY,
      },
    });

    this.setupResourceHandlers();
    this.setupToolHandlers();
    
    // Error handling
    this.server.onerror = (error) => console.error('[MCP Error]', error);
    process.on('SIGINT', async () => {
      await this.server.close();
      process.exit(0);
    });
  }

  setupResourceHandlers() {
    // List available resources
    this.server.setRequestHandler(ListResourcesRequestSchema, async () => ({
      resources: [
        {
          uri: `tmdb://movie/popular`,
          name: `Popular Movies`,
          mimeType: 'application/json',
          description: 'List of currently popular movies',
        },
        {
          uri: `tmdb://tv/popular`,
          name: `Popular TV Shows`,
          mimeType: 'application/json',
          description: 'List of currently popular TV shows',
        },
      ],
    }));

    // Define resource templates
    this.server.setRequestHandler(
      ListResourceTemplatesRequestSchema,
      async () => ({
        resourceTemplates: [
          {
            uriTemplate: 'tmdb://movie/{id}',
            name: 'Movie details by ID',
            mimeType: 'application/json',
            description: 'Get detailed information about a specific movie by its ID',
          },
          {
            uriTemplate: 'tmdb://tv/{id}',
            name: 'TV show details by ID',
            mimeType: 'application/json',
            description: 'Get detailed information about a specific TV show by its ID',
          },
          {
            uriTemplate: 'tmdb://person/{id}',
            name: 'Person details by ID',
            mimeType: 'application/json',
            description: 'Get detailed information about a specific person by their ID',
          },
        ],
      })
    );

    // Handle resource requests
    this.server.setRequestHandler(
      ReadResourceRequestSchema,
      async (request) => {
        const uri = request.params.uri;
        
        // Handle popular movies resource
        if (uri === 'tmdb://movie/popular') {
          try {
            const response = await this.axiosInstance.get('/movie/popular');
            return {
              contents: [
                {
                  uri,
                  mimeType: 'application/json',
                  text: JSON.stringify(response.data, null, 2),
                },
              ],
            };
          } catch (error) {
            this.handleApiError(error);
          }
        }
        
        // Handle popular TV shows resource
        if (uri === 'tmdb://tv/popular') {
          try {
            const response = await this.axiosInstance.get('/tv/popular');
            return {
              contents: [
                {
                  uri,
                  mimeType: 'application/json',
                  text: JSON.stringify(response.data, null, 2),
                },
              ],
            };
          } catch (error) {
            this.handleApiError(error);
          }
        }
        
        // Handle movie details by ID
        const movieMatch = uri.match(/^tmdb:\/\/movie\/(\d+)$/);
        if (movieMatch) {
          const movieId = movieMatch[1];
          try {
            const response = await this.axiosInstance.get(`/movie/${movieId}`);
            return {
              contents: [
                {
                  uri,
                  mimeType: 'application/json',
                  text: JSON.stringify(response.data, null, 2),
                },
              ],
            };
          } catch (error) {
            this.handleApiError(error);
          }
        }
        
        // Handle TV show details by ID
        const tvMatch = uri.match(/^tmdb:\/\/tv\/(\d+)$/);
        if (tvMatch) {
          const tvId = tvMatch[1];
          try {
            const response = await this.axiosInstance.get(`/tv/${tvId}`);
            return {
              contents: [
                {
                  uri,
                  mimeType: 'application/json',
                  text: JSON.stringify(response.data, null, 2),
                },
              ],
            };
          } catch (error) {
            this.handleApiError(error);
          }
        }
        
        // Handle person details by ID
        const personMatch = uri.match(/^tmdb:\/\/person\/(\d+)$/);
        if (personMatch) {
          const personId = personMatch[1];
          try {
            const response = await this.axiosInstance.get(`/person/${personId}`);
            return {
              contents: [
                {
                  uri,
                  mimeType: 'application/json',
                  text: JSON.stringify(response.data, null, 2),
                },
              ],
            };
          } catch (error) {
            this.handleApiError(error);
          }
        }
        
        throw new McpError(
          ErrorCode.InvalidRequest,
          `Invalid URI format: ${uri}`
        );
      }
    );
  }

  setupToolHandlers() {
    this.server.setRequestHandler(ListToolsRequestSchema, async () => ({
      tools: [
        {
          name: 'search_movies',
          description: 'Search for movies by title',
          inputSchema: {
            type: 'object',
            properties: {
              query: {
                type: 'string',
                description: 'Movie title to search for',
              },
              page: {
                type: 'number',
                description: 'Page number for results (1-1000)',
                minimum: 1,
                maximum: 1000,
              },
            },
            required: ['query'],
          },
        },
        {
          name: 'search_tv',
          description: 'Search for TV shows by title',
          inputSchema: {
            type: 'object',
            properties: {
              query: {
                type: 'string',
                description: 'TV show title to search for',
              },
              page: {
                type: 'number',
                description: 'Page number for results (1-1000)',
                minimum: 1,
                maximum: 1000,
              },
            },
            required: ['query'],
          },
        },
        {
          name: 'search_people',
          description: 'Search for people by name',
          inputSchema: {
            type: 'object',
            properties: {
              query: {
                type: 'string',
                description: 'Person name to search for',
              },
              page: {
                type: 'number',
                description: 'Page number for results (1-1000)',
                minimum: 1,
                maximum: 1000,
              },
            },
            required: ['query'],
          },
        },
        {
          name: 'get_movie_recommendations',
          description: 'Get movie recommendations based on a movie ID',
          inputSchema: {
            type: 'object',
            properties: {
              movie_id: {
                type: 'number',
                description: 'TMDB movie ID',
              },
              page: {
                type: 'number',
                description: 'Page number for results (1-1000)',
                minimum: 1,
                maximum: 1000,
              },
            },
            required: ['movie_id'],
          },
        },
        {
          name: 'get_trending',
          description: 'Get trending movies, TV shows, or people',
          inputSchema: {
            type: 'object',
            properties: {
              media_type: {
                type: 'string',
                description: 'Media type to get trending items for',
                enum: ['all', 'movie', 'tv', 'person'],
              },
              time_window: {
                type: 'string',
                description: 'Time window for trending items',
                enum: ['day', 'week'],
              },
            },
            required: ['media_type', 'time_window'],
          },
        },
      ],
    }));

    this.server.setRequestHandler(CallToolRequestSchema, async (request) => {
      const { name, arguments: args } = request.params;

      switch (name) {
        case 'search_movies':
          return await this.searchMovies(args);
        case 'search_tv':
          return await this.searchTV(args);
        case 'search_people':
          return await this.searchPeople(args);
        case 'get_movie_recommendations':
          return await this.getMovieRecommendations(args);
        case 'get_trending':
          return await this.getTrending(args);
        default:
          throw new McpError(
            ErrorCode.MethodNotFound,
            `Unknown tool: ${name}`
          );
      }
    });
  }

  async searchMovies(args) {
    if (!this.isValidSearchArgs(args)) {
      throw new McpError(
        ErrorCode.InvalidParams,
        'Invalid search arguments'
      );
    }

    try {
      const response = await this.axiosInstance.get('/search/movie', {
        params: {
          query: args.query,
          page: args.page || 1,
        },
      });

      return {
        content: [
          {
            type: 'text',
            text: JSON.stringify(response.data, null, 2),
          },
        ],
      };
    } catch (error) {
      return this.handleToolError(error);
    }
  }

  async searchTV(args) {
    if (!this.isValidSearchArgs(args)) {
      throw new McpError(
        ErrorCode.InvalidParams,
        'Invalid search arguments'
      );
    }

    try {
      const response = await this.axiosInstance.get('/search/tv', {
        params: {
          query: args.query,
          page: args.page || 1,
        },
      });

      return {
        content: [
          {
            type: 'text',
            text: JSON.stringify(response.data, null, 2),
          },
        ],
      };
    } catch (error) {
      return this.handleToolError(error);
    }
  }

  async searchPeople(args) {
    if (!this.isValidSearchArgs(args)) {
      throw new McpError(
        ErrorCode.InvalidParams,
        'Invalid search arguments'
      );
    }

    try {
      const response = await this.axiosInstance.get('/search/person', {
        params: {
          query: args.query,
          page: args.page || 1,
        },
      });

      return {
        content: [
          {
            type: 'text',
            text: JSON.stringify(response.data, null, 2),
          },
        ],
      };
    } catch (error) {
      return this.handleToolError(error);
    }
  }

  async getMovieRecommendations(args) {
    if (!this.isValidMovieIdArgs(args)) {
      throw new McpError(
        ErrorCode.InvalidParams,
        'Invalid movie ID arguments'
      );
    }

    try {
      const response = await this.axiosInstance.get(`/movie/${args.movie_id}/recommendations`, {
        params: {
          page: args.page || 1,
        },
      });

      return {
        content: [
          {
            type: 'text',
            text: JSON.stringify(response.data, null, 2),
          },
        ],
      };
    } catch (error) {
      return this.handleToolError(error);
    }
  }

  async getTrending(args) {
    if (!this.isValidTrendingArgs(args)) {
      throw new McpError(
        ErrorCode.InvalidParams,
        'Invalid trending arguments'
      );
    }

    try {
      const response = await this.axiosInstance.get(
        `/trending/${args.media_type}/${args.time_window}`
      );

      return {
        content: [
          {
            type: 'text',
            text: JSON.stringify(response.data, null, 2),
          },
        ],
      };
    } catch (error) {
      return this.handleToolError(error);
    }
  }

  isValidSearchArgs(args) {
    return (
      typeof args === 'object' &&
      args !== null &&
      typeof args.query === 'string' &&
      (args.page === undefined || typeof args.page === 'number')
    );
  }

  isValidMovieIdArgs(args) {
    return (
      typeof args === 'object' &&
      args !== null &&
      typeof args.movie_id === 'number' &&
      (args.page === undefined || typeof args.page === 'number')
    );
  }

  isValidTrendingArgs(args) {
    return (
      typeof args === 'object' &&
      args !== null &&
      typeof args.media_type === 'string' &&
      typeof args.time_window === 'string' &&
      ['all', 'movie', 'tv', 'person'].includes(args.media_type) &&
      ['day', 'week'].includes(args.time_window)
    );
  }

  handleApiError(error) {
    if (axios.isAxiosError(error)) {
      throw new McpError(
        ErrorCode.InternalError,
        `TMDB API error: ${
          error.response?.data.status_message ?? error.message
        }`
      );
    }
    throw error;
  }

  handleToolError(error) {
    if (axios.isAxiosError(error)) {
      return {
        content: [
          {
            type: 'text',
            text: `TMDB API error: ${
              error.response?.data.status_message ?? error.message
            }`,
          },
        ],
        isError: true,
      };
    }
    throw error;
  }

  async run() {
    const transport = new StdioServerTransport();
    await this.server.connect(transport);
    console.error('TMDB MCP server running on stdio');
  }
}

const server = new TMDBServer();
server.run().catch(console.error);
