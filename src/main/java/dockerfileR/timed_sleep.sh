#!/bin/sh

total_time=6
start_time=$(date +%s)

while true; do
    current_time=$(date +%s)
    elapsed_time=$((current_time - start_time))
    
    if [ $elapsed_time -ge $total_time ]; then
        break
    fi
    
    sleep 0.1
done

echo "Finished after $elapsed_time seconds"
