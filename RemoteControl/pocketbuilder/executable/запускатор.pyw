import subprocess
import sys
import threading

def h():
    subprocess.Popen("jre-15\\bin\\java.exe -jar pocketbuilder.jar", shell=True)

threading.Thread(target=h, daemon=True).start()
