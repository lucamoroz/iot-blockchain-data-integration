import time as t

if __name__ == '__main__':
    print("Starting fake sensor")
    for i in range(0, 10):
        print("%d/%d" % (i, 9))
        t.sleep(1)
    print("[end of program]")
