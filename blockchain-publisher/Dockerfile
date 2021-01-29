# Base image
FROM python:3.8

# Install our package manager
RUN pip install pipenv

# Set the working directory
WORKDIR /app

# Copy only the necessary files to install the deps
COPY Pipfile* ./

# Create the requirements file for pip to install the deps
RUN pipenv lock -r > requirements.txt && \
    pip install -r requirements.txt

# Copy rest 
COPY . .