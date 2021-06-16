ANT := ant -lib lib

update:
	$(ANT) resolve
	$(ANT) update-jdk
	$(ANT) build

pkg:	
	$(ANT) resolve
	$(ANT) update-jdk
	$(ANT) build
	$(ANT) clean spk

clean:
	git reset --hard
	git pull
	git --no-pager log -1
