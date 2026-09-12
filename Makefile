.PHONY: fmt

fmt:
	git ls-files | grep "\.java$$" | xargs google-java-format -i
	bun i
	bun run oxfmt

run:
	javac P101BinarySearchTree/*.java && java P101BinarySearchTree.BinarySearchTree
	javac P102BSTRotation/*.java && java P102BSTRotation.BSTRotation

clean:
	mkdir -p /tmp/cs400/01
	cat P101BinarySearchTree/BinarySearchTree.java | grep -v "^package" | sed -e "s/ implements SortedCollection<T>//" >/tmp/cs400/01/BinarySearchTree.java
	$(info "P101: submit /tmp/cs400/01/BinarySearchTree.java")