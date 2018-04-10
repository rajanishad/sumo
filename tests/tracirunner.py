#!/usr/bin/env python
# Eclipse SUMO, Simulation of Urban MObility; see https://eclipse.org/sumo
# Copyright (C) 2008-2018 German Aerospace Center (DLR) and others.
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v20.html
# SPDX-License-Identifier: EPL-2.0

# @file    tracirunner.py
# @author  Friedemann Wesner
# @author  Michael Behrisch
# @author  Jakob Erdmann
# @date    2008-05-12
# @version $Id$

from __future__ import absolute_import
from __future__ import print_function

import os
import subprocess
import sys
import time
THIS_DIR = os.path.abspath(os.path.dirname(__file__))
sys.path.append(os.path.join(THIS_DIR, '..', "tools"))
import sumolib  # noqa

PORT = str(sumolib.miscutils.getFreeSocketPort())
server_args = sys.argv[1:] + ["--remote-port", PORT]
binaryDir, server = os.path.split(server_args[0])

client = "TraCITestClient"
if server.endswith("D") or server.endswith("D.exe"):
    client += "D"
client_args = [os.path.join(binaryDir, client), "-def",
               "testclient.prog", "-o", "testclient_out.txt", "-p", PORT]

# start sumo as server
serverProcess = subprocess.Popen(
    server_args, stdout=sys.stdout, stderr=sys.stderr)
success = False
for retry in range(7):
    time.sleep(retry)
    clientProcess = subprocess.Popen(
        client_args, stdout=sys.stdout, stderr=sys.stderr)
    if serverProcess.poll() is not None and clientProcess.poll() is None:
        # the server is already through (either with error or success), let the client catch up
        time.sleep(10)
        if serverProcess.poll() is not None and clientProcess.poll() is None:
            print("Client hangs but server terminated for unknown reason", file=sys.stderr)
            clientProcess.kill()
            break
    result = clientProcess.wait()
    if result == 0:
        success = True
    if result != 2:
        break

if success:
    serverProcess.wait()
else:
    if serverProcess.poll() is None:
        print ("Server hangs and does not answer connection requests", file=sys.stderr)
        serverProcess.kill()
