(ns hcl.core-test
  (:require [hcl.core :as sut]
            [clojure.test :refer :all]))

(deftest repeated-blocks
  (is (= (-> {[:resource "aws_security_group" "server"]
              {:name "allow_traffic"
               :description "Allow traffic to SSH and SSL ports"
               :vpc_id "${aws_vpc.main.id}"
               :r/ingress [{:from_port "22"
                            :to_port "22"
                            :protocol "tcp"
                            :cidr_blocks ["0.0.0.0/0"]}
                           {:from_port "443"
                            :to_port "443"
                            :protocol "tcp"
                            :cidr_blocks ["0.0.0.0/0"]}]}}
          sut/emit)
         "resource \"aws_security_group\" \"server\" {
  name = \"allow_traffic\"
  description = \"Allow traffic to SSH and SSL ports\"
  vpc_id = \"${aws_vpc.main.id}\"
  ingress {
      from_port = \"22\"
      to_port = \"22\"
      protocol = \"tcp\"
      cidr_blocks = [
            \"0.0.0.0/0\"
      ]
  }
  ingress {
      from_port = \"443\"
      to_port = \"443\"
      protocol = \"tcp\"
      cidr_blocks = [
            \"0.0.0.0/0\"
      ]
  }
}
")))
