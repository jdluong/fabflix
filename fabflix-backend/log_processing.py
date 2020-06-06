log = open("src/main/resources/time_log.txt")
lines = log.readlines()
totalTS = 0
totalTJ = 0
numTimes = len(lines)

for line in lines:
    times = line.split(' ')
    totalTS += int(times[0])
    totalTJ += int(times[1])

averageTS = (totalTS / numTimes) / 1000000
averageTJ = totalTJ / numTimes / 1000000

print('Average Search Time: %.2f ms' % averageTS)
print('Average JDBC Time: %.2f ms' % averageTJ)