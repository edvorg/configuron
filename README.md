# configuron

Clojure(Script) [environ](https://github.com/weavejester/environ) compatible config that reloads from `project.clj` when in dev mode.

[![Clojars Project](https://img.shields.io/clojars/v/rocks.clj/configuron.svg)](https://clojars.org/rocks.clj/configuron)

## Why

configuron is a configuration library that has interface of environ, but provides additional features.

- easy to migrate from environ
- env variable is updated every time you update your `project.clj`
- `clojurescript` support. you can access your config on frontend the same way you do it on backend.
- server-side rendering ready design. let's you choose whether you want to get config using ajax or encode it in html page.
- filtering rules for client side config in order to limit information, that's available to the client.

## Usage

### Clojure

1. Add `:mode` keys for each of your profiles in `project.clj`

```clojure
{:profiles {:dev {:env {:mode :dev}}
            :uberjar {:env {:mode :prod}}}}
```

2. Access your config through `rocks.clj.configuron.core/env` instead of `environ.core/env`.

### ClojureScript

You can use your config on client-side as well.
Simply add GET route `/environ` to your project with handler `rocks.clj.configuron.core/config-handler`:
```clojure
(GET "/environ" [] #'config-handler)
```
By default your client-side config will be empty.
To add some data you should add paths to `:client-config-keys`.
For example given your profiles look like this.
```clojure
{:profiles {:dev {:env {:mode :dev
                        :debug-info {:tokens {:sentry ""}}
                        :client-config-keys [[:mode]
                                             [:debug-info :tokens]]}}
            :uberjar {:env {:mode :uberjar
                            :client-config-keys [[:mode]]}}}}
```
`/environ` ring handler will return.

In dev:
```clojure
{:mode :dev
 :debug-info {:tokens {:sentry ""}}}
```
In prod:
```clojure
{:mode :uberjar}
```

You can access your config on client side as usual through `rocks.clj.configuron.core/env`
or dynamically by executing http request to `/environ`.

### Caution

When accessing `rocks.clj.configuron.core/env` on page load (for example cljs app entry point),
there is no guarantee that config has been received by that time.
There are two solutions for that.

1. Wrap your code in go block
```clojure
(go
  (let [env (or (<! rocks.clj.configuron.core/fetcher)
                rocks.clj.configuron.core/env)]
    ;; your web-app initialization goes here
    ))
```

or (preferably)

2. Write your config into dom on server side.
This will also effectively avoid http request to `/environ`.

Example in html:

```html
<body>
  <div id="config" transit="your config goes here" />
</body>
```

Example in hiccup:

```clojure
[:body
 [:div#config {:transit (rocks.clj.configuron.core/get-client-config)}]]
```

## License

Copyright © 2018 Eduard Knyshov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
