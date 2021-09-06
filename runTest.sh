#!/bin/bash

echo Starting test
echo $1" "$2
mvn -Dcucumber.filter.tags="${2}" clean test