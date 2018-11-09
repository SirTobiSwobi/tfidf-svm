package org.SirTobiSwobi.c3.tfidfsvm.db;

import java.util.ArrayList;

public class AVLTreeNode<T> {
	private AVLTreeNode<T> left, right, parent;
	private AVLTree<T> tree;
	private T content;
	private long index;
	
	public AVLTreeNode(AVLTree<T> tree, T content, long index) {
		super();
		this.tree = tree;
		this.content = content;
		this.index = index;
	}

	public AVLTreeNode(AVLTreeNode<T> parent, AVLTree<T> tree, T content, long index) {
		super();
		this.parent = parent;
		this.tree = tree;
		this.content = content;
		this.index = index;
	}
	
	public T getContent(long id){
		T content=null;
		if(id==this.index){
			content=this.content;
		}else if(id<this.index&&left!=null){
			content=left.getContent(id);
		}else if(id>this.index&&right!=null){
			content=right.getContent(id);
		}	
		return content;
	}
	
	protected AVLTreeNode<T> getById(long id){
		AVLTreeNode<T> node=null;
		if(id==this.index){
			node=this;
		}else if(id<this.index&&left!=null){
			node=left.getById(id);
		}else if(id>this.index&&right!=null){
			node=right.getById(id);
		}	
		return node;
	}
	
	public long getIndex() {
		return index;
	}
	
	public boolean containsId(long id){
		boolean contains = false;
		if(this.index==id){
			return true;
		}else if(id < this.index && left!=null){
			contains=left.containsId(id);
		}else if(id > this.index && right!=null){
			contains=right.containsId(id);
		}	
		return contains;
	}
	
	protected void getAllContent(ArrayList<T> allContent){
		if(left!=null){
			left.getAllContent(allContent);
		}
		allContent.add(content);
		if(right!=null){
			right.getAllContent(allContent);
		}
	}

	public synchronized void setContent(long id, T content){
		if(id==this.index){
			this.content=content;
		}else if(id<this.index&&left==null){
			left = new AVLTreeNode<T>(this,tree,content,id);
			tree.rebalance(id);
			tree.incrementSize();
		}else if(id<this.index&&left!=null){
			left.setContent(id, content);
		}else if(id>this.index&&right==null){
			right = new AVLTreeNode<T>(this,tree,content,id);
			tree.rebalance(id);
			tree.incrementSize();
		}else if(id>this.index&&right!=null){
			right.setContent(id,content);
		}
	}

	public AVLTreeNode<T> getLeft() {
		return left;
	}

	public void setLeft(AVLTreeNode<T> left) {
		this.left = left;
	}

	public AVLTreeNode<T> getRight() {
		return right;
	}

	public void setRight(AVLTreeNode<T> right) {
		this.right = right;
	}

	public AVLTreeNode<T> getParent() {
		return parent;
	}

	public void setParent(AVLTreeNode<T> parent) {
		this.parent = parent;
	}
	
	public int getHeight(){
		int height=0;
		if(left!=null&&right==null){
			height=height+1+left.getHeight();
		}else if(left==null&&right!=null){
			height=height+1+right.getHeight();
		}else if(left!=null&&right!=null){
			if(left.getHeight()>=right.getHeight()){
				height=height+1+left.getHeight();
			}else if(left.getHeight()<right.getHeight()){
				height=height+1+right.getHeight();
			}
		}
		return height;
	}
	
	public boolean isBalanced(){
		boolean isBalanced=false;
		if(left==null&&right==null){
			isBalanced=true;
		}else if(left!=null&&right==null&&left.getHeight()<2){
			isBalanced=true;
		}else if(left==null&&right!=null&&right.getHeight()<2){
			isBalanced=true;
		}else if(left!=null&&right!=null&&Math.abs(left.getHeight()-right.getHeight())<2){
			isBalanced=true;
		}
		return isBalanced;
	}
	
	public boolean treeIsBalanced(){
		boolean isBalanced=false;
		if(this.isBalanced()){
			if(left==null&&right==null){
				isBalanced=true;
			}else if(left!=null&&right==null){
				isBalanced=left.treeIsBalanced();
			}else if(left==null&&right!=null){
				isBalanced=right.treeIsBalanced();
			}else if(left!=null&&right!=null){
				isBalanced=left.treeIsBalanced()&&right.treeIsBalanced();
			}
		}
		return isBalanced;
	}
	
	public int getBalance(){
		int balance=0;
		if(left==null&&right!=null){
			balance=0-right.getHeight();
		}else if(left!=null&&right==null){
			balance=left.getHeight();
		}else if(left!=null&&right!=null){
			balance = left.getHeight()-right.getHeight();
		}
		return balance;
	}
	
	public AVLTreeNode<T> getUnbalancedAncestor(long id){
		AVLTreeNode<T> unbalancedAncestor = null;
		if(this.index==id){
			unbalancedAncestor=this.getUnbalancedAncestor();
		}else if(id<this.index){
			unbalancedAncestor=left.getUnbalancedAncestor(id);
		}else if(id>this.index){
			unbalancedAncestor=right.getUnbalancedAncestor(id);
		}		
		return unbalancedAncestor;
	}
	
	private AVLTreeNode<T> getUnbalancedAncestor(){
		AVLTreeNode<T> unbalancedAncestor = null;
		if(this.isBalanced()){
			unbalancedAncestor=parent.getUnbalancedAncestor();
		}else{
			unbalancedAncestor=this;
		}	
		return unbalancedAncestor;
	}
	
	public long getMaxId(){
		if(right!=null){
			return right.getMaxId();
		}
		return index;
	}
	
	public synchronized void deleteNode(long id){
		if(id==this.index){
			//to do: Delete me. Different if I'm the root or not. 
			if(tree.getRoot().equals(this)){
				//I am (G)root lol
				if(left==null&&right==null){
					//I am a leaf and the root. Deleting me makes the tree empty!
					tree.emptyTree();
				}else if(left==null&&right!=null){
					//I am the root and only have a right child. I must make it root. 
					tree.setRoot(right);
				}else if(left!=null&&right==null){
					//I am the root and only have a left child. I must make it root.
					tree.setRoot(left);
				}else if(left!=null&&right!=null){
					//I am the root and have to children.
					//In order to delete myself, I have to copy the content of my in order successor to myself and delete the inorder successor
					AVLTreeNode<T> inOrderSuccessor = right.findMinAntecessor();
					this.index=inOrderSuccessor.getIndex();
					this.content=inOrderSuccessor.getContent(inOrderSuccessor.getIndex());
					right.deleteNode(inOrderSuccessor.getIndex());
					tree.rebalanceUnbalancedNode(this.getIndex()); //rebalancing the new root
				}
				
			}else{
				//Am I a leaf, do I have one child or do I have two children?
				if(left==null&&right==null){
					//I am a leaf, and I am not root
					if(parent.getLeft()!=null&&parent.getLeft().equals(this)){
						//I am a leaf, and my parent's left child
						parent.setLeft(null);
					}else if(parent.getRight()!=null&&parent.getRight().equals(this)){
						//I am a leaf and my parent's right child
						parent.setRight(null);
					}	
				}else if(left==null&&right!=null){
					//I have one right child and I am not root
					if(parent.getLeft()!=null&&parent.getLeft().equals(this)){
						//I have one right child and am my parent's left child
						//So I replace my reference with that of my child
						parent.setLeft(right);
					}else if(parent.getRight()!=null&&parent.getRight().equals(this)){
						//I have one right child and am my parent's right child
						//So I replace my reference with that of my child
						parent.setRight(right);
					}
				}else if(left!=null&&right==null){
					//I have one left child and I am not root
					if(parent.getLeft()!=null&&parent.getLeft().equals(this)){
						//I have one left child and am my parent's left child
						//So I replace my reference with that of my child
						parent.setLeft(left);
					}else if(parent.getRight()!=null&&parent.getRight().equals(this)){
						//I have one left child and am my parent's right child
						//So I replace my reference with that of my child
						parent.setRight(left);	
					}
				}else if(left!=null&&right!=null){
					//I have two children and I am not root
					//In order to delete myself, I have to copy the content of my in order successor to myself and delete the inorder successor
					AVLTreeNode<T> inOrderSuccessor = right.findMinAntecessor();
					this.index=inOrderSuccessor.getIndex();
					this.content=inOrderSuccessor.getContent(inOrderSuccessor.getIndex());
					right.deleteNode(inOrderSuccessor.getIndex());
				}
				if(!tree.getRoot().isBalanced()){ //only search for unbalanced ancestors if they can be found! Otherwise Stack Overflow
					tree.rebalanceUnbalancedNode(this.getUnbalancedAncestor().getIndex());
				}		
			}
		}else if(id<index&&left!=null){
			left.deleteNode(id);
		}else if(id>index&&right!=null){
			right.deleteNode(id);
		}
	}
	
	private AVLTreeNode<T> findMinAntecessor(){
		AVLTreeNode<T> minId=this;
		if(left!=null){
			minId=left.findMinAntecessor();
		}
		return minId;
	}

	protected void populateUsedIds(ArrayList<Long> usedIds){
		if(left!=null){
			left.populateUsedIds(usedIds);
		}
		if(!usedIds.contains(index)){
			usedIds.add(index);
		}
		if(right!=null){
			right.populateUsedIds(usedIds);
		}
	}
}
