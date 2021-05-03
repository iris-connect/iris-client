#!/usr/bin/env python3

import os
import subprocess

here = os.path.dirname(os.path.realpath(__file__))
os.chdir(here)

os.system('./minikube-check-dependencies.py')

is_running = True
try:
    status = str(subprocess.check_output(['minikube', 'status']))

    expected_parts = [
        'host: Running',
        'kubelet: Running',
        'apiserver: Running',
        'kubeconfig: Configured',
    ]
    for expected in expected_parts:
        if expected not in status:
            is_running = False
except subprocess.CalledProcessError as e:
    if e.returncode == 7:
        is_running = False
    else:
        raise e

if not is_running:
    os.system('minikube start')
