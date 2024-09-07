# Using the OpenAI, Anthropic, Mistral, and Local Hugging Face Large Language Model APIs in Racket

A Clojure library for using the Ollama LLM APIs

You need to install Ollama on your system: https://ollama.ai

You then need to install the Mistral model (this takes a while the first time, but the model file is cached so future startups are fast):

    ollama run mistral


## Usage

In one console, run the Ollama REST API service:

    ollama serve

Then run the tests in another window:

    lein test

## Code for my book "Practical Artificial Intelligence Programming With Clojure"

You read my eBooks for free, see my
website [https://markwatson.com](https://markwatson.com). If you would like to pay me for a copy then please visit [https://leanpub.com/clojureai](https://leanpub.com/clojureai).

## License

Copyright © 2021 Mark Watson

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
