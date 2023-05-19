#!/bin/bash


echo "PostgreSQL INIT..."

psql -c 'create database testDB'

echo "PostgreSQL INIT...Done"