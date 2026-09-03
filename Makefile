.PHONY: fmt

fmt:
	git ls-files | grep "\.java$$" | xargs google-java-format -i
	bun i
	bun run oxfmt

clean:
	cat P101BinarySearchTree/BinarySearchTree.java | grep -v "^package" | sed -e "s/ implements SortedCollection<T>//" >~/Downloads/BinarySearchTree.java
	$(info "P101: submit ~/Downloads/BinarySearchTree.java")