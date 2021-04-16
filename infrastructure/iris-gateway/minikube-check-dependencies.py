#!/usr/bin/env python3

import os
from shutil import which

here = os.path.dirname(os.path.realpath(__file__))
os.chdir(here)

dependencies = [
    'minikube',
    'kubectl',
    'helm',
]
missing_dependencies = []

for dependency in dependencies:
    if which(dependency) is None:
        missing_dependencies.append(dependency)

if missing_dependencies:
    print('Missing dependencies:', missing_dependencies)
    exit(1)
