FROM nvidia/cuda:11.8.0-cudnn9-runtime-ubuntu22.04

# Install miniconda
ENV CONDA_DIR /opt/conda
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*
RUN wget --quiet https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh -O ~/miniconda.sh && \
    /bin/bash ~/miniconda.sh -b -p $CONDA_DIR && \
    rm ~/miniconda.sh
ENV PATH=$CONDA_DIR/bin:$PATH

# Copy environment file
COPY TRACER/TRACER/yolo_environments.yaml /tmp/environment.yml

# Create conda environment
RUN conda env create -f /tmp/environment.yml

# Make RUN commands use the new environment
SHELL ["conda", "run", "-n", "yolo", "/bin/bash", "-c"]

# Copy application code
COPY . .

# The code to run when container is started
ENTRYPOINT ["conda", "run", "--no-capture-output", "-n", "yolo", "python", "app.py"]
