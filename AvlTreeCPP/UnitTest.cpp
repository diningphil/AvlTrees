#include "stdafx.h"
#include <iostream>
#include <thread>
#include <chrono>
#include <sstream>
#include "dos.h"
#include "AvlTree.h"

using namespace chrono;
using namespace this_thread;

class MyInteger {

public:
	int val;

	MyInteger(int l) {
		val = l;
	}

	bool operator>(const MyInteger i1) {
		return (val > i1.val);
	}

	bool operator<(const MyInteger& i1) {
		return (val < i1.val);
	}

	bool operator==(const MyInteger& i1) {
		return (val == i1.val);
	}

	string to_string() {
		string Result;      

		ostringstream convert;

		convert << val;    

		Result = convert.str(); 

		return Result;
	}
};

int main() {

	AvlTree<MyInteger>* tree = new AvlTree<MyInteger>();
	MyInteger* myInt;

	for (int i = 10; i >= 0; i--) {
		myInt = new MyInteger(i);
		if (tree->Insert(myInt)) cout << "Inserted Correctly " << myInt->to_string() << endl;
		else cout << "Not Inserted..." << endl;
	}

	for (int i = 10; i >= 0; i--) {
		myInt = new MyInteger(i);
		if (tree->Insert(myInt)) cout << "Inserted Correctly " << myInt->to_string() << endl;
		else cout << "Not Inserted..." << endl;
	}


	for (int i = 10; i >= 0; i--) {
		myInt = new MyInteger(i);
		if (tree->Search(myInt) != NULL)
			cout << "Found Element!" << endl;
		else
			cout << "Not found... " << myInt->to_string() << endl;
	}
	myInt = new MyInteger(5);
	if (tree->Delete(myInt))
		cout << "Deleted Element!" << endl;
	else
		cout << "Not deleted..." << endl;

	myInt = new MyInteger(3);
	if (tree->Delete(myInt))
		cout << "Deleted Element!" << endl;
	else
		cout << "Not deleted..." << endl;

	TreeGenerator<MyInteger>* gen = tree->GetGenerator();
	while (gen->hasNext()) {
		cout << "Visited " << gen->visitNext()->GetNodeValue()->to_string() << endl;
	}

	sleep_for(milliseconds(10000));
	return 0;
}