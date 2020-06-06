import sys

file_name = sys.argv[1]
log = open(str(file_name))
lines = log.readlines()
totalTS = 0
totalTJ = 0
num_times = len(lines)

for line in lines:
    times = line.split(' ')
    totalTS += int(times[0])
    totalTJ += int(times[1])

averageTS = (totalTS / num_times) / 1000000
averageTJ = totalTJ / num_times / 1000000

print('Average Search Time: %.2f ms' % averageTS)
print('Average JDBC Time: %.2f ms' % averageTJ)