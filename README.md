# configuron

Simple environ based config that reloads itself from `project.clj` when in dev mode.

[![Clojars Project](https://img.shields.io/clojars/v/rocks.clj/configuron.svg)](https://clojars.org/rocks.clj/configuron)

## Usage

1. Add `:mode` keys for each of your profiles in `project.clj`

```clojure
{:profiles {:dev {:env {:mode :dev}}
            :uberjar {:env {:mode :prod}}}}
```

2. Access your config through `rocks.clj.configuron.core/env` instead of `environ.core/env`.

## License

Copyright © 2018 Eduard Knyshov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
