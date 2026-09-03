.PHONY: fmt

fmt:
	git ls-files | grep "\.java$$" | xargs google-java-format -i
	bun i
	bun run format
