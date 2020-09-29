// --== CS400 File Header Information ==--
// Name: Ronnie Inglett & Keegan Ripley
// Email: ringlett@wisc.edu
// Team: EE
// TA: Keren Chen
// Lecturer: Florian Heimerl
// Notes to Grader: This class is meant to do the work of setting up the hash table for the incoming books. This has some preliminary stuff, so it will
// need to be constructed by the user.
//
// IMPORTANT: When calling an isbn key with leading zeros, you MUST remove them. Hashcode will tell the difference and give you unintended results. This
// particular feature was discovered late into the project timeline and does not cause issues for this particular implementation, as the BookStoreDriver
// unintentionally chops those digits off. In fact, this probably would have gone undiscovered if not for the way that we handled isbn inputs.


import java.util.NoSuchElementException;
public class BookStore extends BookCollection
{
	private HashTableMap<Integer, Book> table;
	private int numBooks;
	
	/**
	 * Constructor that sets up a HashTable for the book store. Begins with default size 20.
	 */
	public BookStore()
	{
		super(20);
		numBooks = 0;
		Book[] books = getInitialBooks();
		
		table = new HashTableMap<Integer, Book>(20);
		
		// Put the objects from Book.txt into the hash table
		for (int i = 0; i < books.length && books[i] != null; ++i)
		{
			numBooks = numBooks + books[i].getQuantity();
			// If the object already exists in the hash table, we want to update the quantity of that item in the hash table. This ends up being somewhat
			// of a lengthy statement, because you have to reference the key of the book multiple times. However I didn't feel like making a new variable.
			if (table.containsKey(books[i].getIsbn()))
			{
				table.get(books[i].getIsbn()).setQuantity(table.get(books[i].getIsbn()).getQuantity() + books[i].getQuantity());
			}
			else
				table.put(books[i].getIsbn(), books[i]);
			
		}
	}

	/**
	 * Allows user to determine if a book exists by looking it up by it's ISBN
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean containsBook(int isbn)
	{
		// Check to see if the hash table we created earlier has the key using the HashTableMap containsKey() method.
		if (table.containsKey(isbn))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a book to the hash table, using the HashTableMap put method.
	 * 
	 * @param key
	 * @param book
	 * @return boolean : confirmation that the book was inserted
	 */
	public boolean donate(int key, Book book)
	{
		numBooks = numBooks + book.getQuantity();

		// If the object already exists in the hash table, we want to update the quantity of that item in the hash table. This ends up being somewhat
		// of a lengthy statement.
		if (table.containsKey(key))
		{
			table.get(key).setQuantity(table.get(key).getQuantity() + book.getQuantity());
		}
		else
			table.put(key, book);
		
		return (containsBook(key));
	}
	
	/**
	 * Simple method that removes a SINGLE book from our hash table. Returns the given book's title as confirmation.
	 * 
	 * @param key
	 * @return String title
	 */
	public String remove(int key)
	{
		// Check that the book exists.
		if (containsBook(key))
		{
			// Set up a return string
			Book removed = table.get(key);
			String confirmation = "Book bought: " + removed.getTitle();
		
			// Update the quantity accordingly
			if (table.get(key).getQuantity() < 2)
				table.remove(key);
			else
				table.get(key).setQuantity(table.get(key).getQuantity() - 1);
			--numBooks;
		
		return confirmation;
		}
		else
			return "No such book in stock.";
	}
	
	/**
	 * Clears the hash table if the book store is robbed.
	 */
	public void steal()
	{
		table.clear();
		numBooks=0;
		System.out.println("The store has been robbed. How tragic.");
	}
	
	/**
	 * Returns the current amount of books.
	 */
	public int size()
	{
		return numBooks;
	}
	
	/**
	 * Gets a given book, given a key.
	 * @param key
	 * @return Book : object and all of it's associated things
	 * @throws NoSuchElementException
	 */
	public Book getBook(int key) throws NoSuchElementException
	{
		// table.get already throws an exception, so no need to check.
		return table.get(key);
	}
}
