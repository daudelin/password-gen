(ns pg-test.core
  (:require [clojurewerkz.scrypt.core :as scrypt])
  (:import  [java.security SecureRandom]
            [com.lambdaworksfork.crypto SCryptUtil]
            [com.pg_test.crypto HashUtil]
            [com.lambdaworks.codec Base64]))

(defrecord SCryptCostParams [^long N, ^long r, ^long p])

(defrecord PasswordConstraints [^int plen, ^int minLowers, ^int minUppers, ^int minDigits, ^int minSymbols])

(defn gen-salt []
  (let [prng (SecureRandom/getInstance "SHA1PRNG")
        bytes (byte-array 16)]
    (.nextBytes prng bytes)
    bytes))

(defn encrypt
  ([^String s]
   (scrypt/encrypt s 16384 8 1))
  ([^String s h cost-params]
   (SCryptUtil/scrypt s h (:N cost-params) (:r cost-params) (:p cost-params))))

(defn gen-password
  [^String hash constraints]
  (HashUtil/genPassword hash
                        (:plen constraints)
                        (:minLowers constraints)
                        (:minUppers constraints)
                        (:minDigits constraints)
                        (:minSymbols constraints)))
