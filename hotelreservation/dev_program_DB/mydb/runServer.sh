#!/bin/bash
# Copyright(C) 2007 National Institute of Informatics, All rights reserved.

# Start the HSQLDB server with the specified database
java -classpath ../lib/hsqldb.jar org.hsqldb.Server -database mydb
