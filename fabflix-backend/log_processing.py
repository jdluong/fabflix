log = open("src/main/resources/time_log.txt")
lines = log.readlines()
totalTime = 0
numTimes = len(lines)

for line in lines:
    totalTime += int(line)

average = totalTime / numTimes

print('Average Search Time: %.2f ns' % average)