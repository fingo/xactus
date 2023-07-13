/*******************************************************************************
 * Copyright (c) 2011 Jesper Moller, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Moller - initial API and implementation
 *     Jesper Steen Moller  - bug 340933 - Migrate to new XPath2 API
 *******************************************************************************/

package info.fingo.xactus.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import info.fingo.xactus.api.typesystem.ItemType;
import info.fingo.xactus.processor.internal.types.SimpleAtomicItemTypeImpl;
import info.fingo.xactus.processor.internal.types.builtin.BuiltinTypeLibrary;
import info.fingo.xactus.processor.internal.types.builtin.SingleItemSequence;

/**
 * @since 2.0
 */
public class ResultBuffer {

	private ArrayList<Item> values = new ArrayList<>();

	public ResultSequence getSequence() {
		if (values.size() == 0) return EMPTY;
		if (values.size() == 1) return wrap(values.get(0));

		return new ArrayResultSequence(values.toArray(new Item[values.size()]));
	}

	public void clear() {
		values.clear();
	}

	public ResultBuffer add(Item at) {
		values.add(at);
		return this;
	}

	public ResultBuffer concat(ResultSequence rs) {
		values.addAll(collectionWrapper(rs));
		return this;
	}

	public int size() {
		return values.size();
	}

	public ListIterator<Item> iterator() {
		return values.listIterator();
	}

	public void prepend(ResultSequence rs) {
		values.addAll(0, collectionWrapper(rs));
	}

	public Collection<Item> getCollection() {
		return this.values;
	}

	public ResultBuffer concat(Collection<Item> others) {
		this.values.addAll(others);
		return this;
	}

	public static ResultSequence wrap(Item item) {
		if (item instanceof SingleItemSequence)
			return (SingleItemSequence)item;

		return new SingleResultSequence(item);
	}

	public Item item(int index) {
		return values.get(index);
	}

	public void addAt(int pos, Item element) {
		values.add(pos, element);
	}

	public static final class SingleResultSequence implements ResultSequence {

		public SingleResultSequence(Item at) {
			value = at;
		}

		private final Item value;

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#getLength()
		 */
		public int size() {
			return 1;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#getItem(int)
		 */
		public Item item(int index) {
			if (index != 0) throw new IndexOutOfBoundsException("Length is one, you looked up number "+ index);
			return value;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#first()
		 */
		public Item first() {
			return item(0);
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#getItem(int)
		 */
		public Object value(int index) {
			if (index != 0) throw new IndexOutOfBoundsException("Length is one, you looked up number "+ index);
			return value.getNativeValue();
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#empty()
		 */
		public boolean empty() {
			return false;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#first()
		 */
		public Object firstValue() {
			return value.getNativeValue();
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#iterator()
		 */
		public Iterator<Item> iterator() {
			
			return new Iterator<Item>() {
				boolean seenIt = false;

				public final void remove() {
					throw new UnsupportedOperationException("ResultSequences are immutable");
				}

				public final Item next() {
					if (! seenIt) {
						seenIt = true;
						return value;
					}
					throw new IllegalStateException("This iterator is at its end");
				}

				public final boolean hasNext() {
					return !seenIt;
				}
			};
		}

		@Override
		public ItemType itemType(int index) {
			return item(index).getItemType();
		}

		@Override
		public ItemType sequenceType() {
			return value.getItemType();
		}
		
	}

	public static final class ArrayResultSequence implements ResultSequence {

		private final Item[] results;

		public ArrayResultSequence(Item results[]) {
			this.results = results;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#getLength()
		 */
		public int size() {
			return results.length;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#getItem(int)
		 */
		public Item item(int index) {
			if (index < 0 && index >= results.length) throw new IndexOutOfBoundsException("Index " + index + " is out of alllowed bounds (less that " + results.length);
			return results[index];
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#empty()
		 */
		public boolean empty() {
			return false;
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#first()
		 */
		public Object firstValue() {
			return item(0).getNativeValue();
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#first()
		 */
		public Item first() {
			return item(0);
		}

		/* (non-Javadoc)
		 * @see info.fingo.xactus.api.ResultSequence#iterator()
		 */
		public Iterator<Item> iterator() {
			
			return new Iterator<Item>() {
				int nextIndex = 0;

				public final void remove() {
					throw new UnsupportedOperationException("ResultSequences are immutable");
				}

				public final Item next() {
					if (nextIndex < results.length) {
						return results[nextIndex++];
					}
					throw new IllegalStateException("This iterator is at its end");
				}

				public final boolean hasNext() {
					return nextIndex < results.length;
				}
			};
		}

		public ItemType itemType(int index) {
			if (index < 0 && index >= results.length) throw new IndexOutOfBoundsException("Index " + index + " is out of alllowed bounds (less that " + results.length);
			return results[index].getItemType();
		}

		public ItemType sequenceType() {
			return new SimpleAtomicItemTypeImpl(BuiltinTypeLibrary.XS_ANYTYPE, ItemType.OCCURRENCE_ONE_OR_MANY);
		}

		public Object value(int index) {
			return item(index).getNativeValue();
		}
		
	}

	private Collection<Item> collectionWrapper(final ResultSequence rs) {
		
		// This is a dummy collections, solely exists for faster inserts into our array
		return new Collection<Item>() {

			@Override
			public boolean add(Item arg0) {
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends Item> arg0) {
				return false;
			}

			@Override
			public void clear() {
			}

			@Override
			public boolean contains(Object arg0) {
				return false;
			}
			
			@Override
			public boolean containsAll(Collection<?> arg0) {
				return false;
			}

			@Override
			public boolean isEmpty() {
				return rs.empty();
			}

			@Override
			public Iterator<Item> iterator() {
				return rs.iterator();
			}

			@Override
			public boolean remove(Object arg0) {
				return false;
			}

			@Override
			public boolean removeAll(Collection<?> arg0) {
				return false;
			}

			@Override
			public boolean retainAll(Collection<?> arg0) {
				return false;
			}

			@Override
			public int size() {
				return rs.size();
			}

			@Override
			public Object[] toArray() {
				return toArray(new Item[size()]);
			}

			public Object[] toArray(Object[] arg0) {
				if (arg0.length < size())
					arg0 = new Item[size()];
				for (int i = 0; i< size(); ++i) {
					arg0[i] = rs.item(i);
				}
				return arg0;
			}
			
		};
	}

	public final static ResultSequence EMPTY = new ResultSequence() {

		@Override
		public int size() {
			return 0;
		}

		@Override
		public Item item(int index) {
			throw new IndexOutOfBoundsException("Sequence is empty!");
		}

		@Override
		public boolean empty() {
			return true;
		}

		@Override
		public ItemType itemType(int index) {
			throw new IndexOutOfBoundsException("Sequence is empty!");
		}

		@Override
		public ItemType sequenceType() {
			return new SimpleAtomicItemTypeImpl(BuiltinTypeLibrary.XS_ANYTYPE, ItemType.OCCURRENCE_ONE_OR_MANY);
		}

		@Override
		public Object value(int index) {
			throw new IndexOutOfBoundsException("Sequence is empty!");
		}

		@Override
		public Object firstValue() {
			throw new IndexOutOfBoundsException("Sequence is empty!");
		}

		@Override
		public Item first() {
			throw new IndexOutOfBoundsException("Sequence is empty!");
		}

		public Iterator<Item> iterator() {
			
			return new Iterator<Item>() {

				@Override
				public Item next() {
					throw new IllegalStateException("This ResultSequence is empty");
				}

				@Override
				public boolean hasNext() {
					return false;
				}
				
				@Override
				public void remove() {
					throw new UnsupportedOperationException("ResultSequences are immutable");
				}
				
			};
		}
		
	};
	
}
