{ pkgs }: {
    deps = [
      pkgs.leiningen
        pkgs.clojure
        pkgs.clojure-lsp
    ];
}