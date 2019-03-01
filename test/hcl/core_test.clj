(ns hcl.core-test
  (:require [hcl.core :as sut]
            [clojure.test :refer :all]))

(deftest repeated-blocks
  (is (= (-> {[:resource "aws_security_group" "server"]
           {:name "allow_traffic"
            :repeated/ingress [{:from_port "22"
                                :to_port "22"}
                               {:from_port "80"
                                :to_port "80"}]}}
          sut/emit)
         "resource \"aws_security_group\" \"server\" {
  name = \"allow_traffic\"
  ingress {
      from_port = \"22\"
      to_port = \"22\"
  }
  ingress {
      from_port = \"80\"
      to_port = \"80\"
  }
}
")))
