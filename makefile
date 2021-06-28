ANT := ant -lib lib -v

update:
	$(ANT) clean
	$(ANT) resolve
	$(ANT) update-jdk
	$(ANT) replaceTokens
	$(ANT) build

pkg:	
	$(ANT) spk

clean:
	git reset --hard
	git pull
	git --no-pager log -1
