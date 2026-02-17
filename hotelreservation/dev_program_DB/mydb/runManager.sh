#!/bin/bash
# Copyright(C) 2007 National Institute of Informatics, All rights reserved.

# Run the HSQLDB Database Manager
java -classpath ../lib/hsqldb.jar org.hsqldb.util.DatabaseManager -url jdbc:hsqldb:hsql://localhost
