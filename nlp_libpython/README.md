# python_interop_deeplearning

This project contains 3 demos that I hope will be easily hackable and thus
useful to your projects:

- Using the spaCY NLP library
- Using the Hugging Face Transformer library for question answering
- Combined spaCy and Hugging Face Transformer library question answering demo

Note: the last example (Combined spaCy and Hugging Face Transformer library question answering demo)
uses material from later in the book on Knowledge Graph and semantic web technologies. I use a bit of
code developed later in the book without explanation to get descriptive text for people,
locations, and organizations.

## Installation

You will probably want to use either a Docker container of a separate VPS server.

Please see INSTALL_MLW.txt for my notes on setting up a GCP VPS.

There is no reason why you couldn't just install everything on your laptop
(but expect difficulties if you use an M1 ARM MacBook), but I personally like
using VPS servers that I can turn on just when I need them: low cost and
convenient.

## Usage

    lein run

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
