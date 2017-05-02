(defproject pg-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/pg-test"]
  :java-source-paths ["src/java"]

  :test-paths ["test/pg-test"]

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/scrypt "1.2.0"]
                 [com.lambdaworks/scrypt "1.4.0"]]
  :profiles {:dev {:repl-options {:init-ns pg-test.core}}}
  )
