.PHONY: fmt runBackendTests run clean

fmt:
	git ls-files | grep "\.java$$" | xargs google-java-format -i
	bun i
	bun run oxfmt

run:
	javac P101BinarySearchTree/*.java && java P101BinarySearchTree.BinarySearchTree
	cd P102BSTRotation && javac *.java && java BSTRotation
	stat junit5.jar || wget https://pages.cs.wisc.edu/~cs400/junit5.jar
	cd P103RoleCode && javac -cp .:../junit5.jar *.java && java -jar ../junit5.jar -cp . -c BackendTests

clean:
	mkdir -p /tmp/cs400/01
	cat P101BinarySearchTree/BinarySearchTree.java | grep -v "^package" | sed -e "s/ implements SortedCollection<T>//" >/tmp/cs400/01/BinarySearchTree.java
	$(info "P101: submit /tmp/cs400/01/BinarySearchTree.java")
