# Try the CLojure LLM library Bosquet

Currently uses Bosquet's defaults:

- Model: gpt-3.5-turbo
- OpenAI API

## Example files

project-root/
├── deps.edn
├── src/
│   └── llm_bosquet/
│       └── core.clj
└── test/
    └── llm_bosquet/
        └── core_test.clj

## Running the example

Make sure you define a valif OpenAI API key:

    export OPENAI_API_KEY=sk-......

Run the tests:

   clj -X:test

# THIS EXAMPE IS WORK IN PROGRESS - NOT YET IN BOOK!

