#!/bin/bash

# Activate Conda environment
source $(conda info --base)/etc/profile.d/conda.sh
conda activate imagerun

# Run the Python API
python prototype_api.py
