ANT := ant -lib lib

update:
	$(ANT) resolve
	$(ANT) update-jdk
	$(ANT) build

pkg:	
	$(ANT) clean
	$(ANT) resolve
	$(ANT) update-jdk
	$(ANT) build
	$(ANT) spk

clean:
	git reset --hard
	git pull
	git --no-pager log -1
