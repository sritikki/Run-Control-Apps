#!/usr/bin/env bash
# Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2023
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#   http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# Get the Grammar repo if it is not there
# Also use the presence of the grammar to manage the jars
if [ ! -d "../Grammar" ]; 
then
    echo "Clone the grammar"
    git clone git@github.com:genevaers/Grammar.git ../Grammar
    cd ../Grammar
    mvn install
    cd ../prebuild
    echo "Configure Build"
    ./mvnInstallJars.sh
else
    echo "Grammar repo in place"
fi
echo "Build"
