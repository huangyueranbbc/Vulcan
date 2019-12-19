package gg;

import java.util.ArrayList;
import java.util.List;

public class ManyNodeTree {

	/** 树根 */
	private ManyTreeNode root;

	public ManyNodeTree() {
		root = new ManyTreeNode(new TreeNode("root"));
	}

	// 生成一个多茶树，根节点为root
	public ManyNodeTree createTree(List<TreeNode> treeNodes) {

		if (treeNodes == null || treeNodes.size() <= 0) {
			return null;
		}

		ManyNodeTree manyNodeTree = new ManyNodeTree();
		// 将所有节点添加到多茶树中
		for (TreeNode treeNode : treeNodes) {
			if (treeNode.getParentId().equals("root")) {
				manyNodeTree.getRoot().getChildList().add(new ManyTreeNode(treeNode));
			} else {
				addChild(manyNodeTree.getRoot(), treeNode);
			}
		}
		return manyNodeTree;
	}

	// 向指定多叉树节点添加子节点
	public void addChild(ManyTreeNode manyTreeNode, TreeNode child) {
		for (ManyTreeNode item : manyTreeNode.getChildList()) {
			if (item.getData().getNodeId().equals(child.getParentId())) {
				// 找到对应的父亲
				item.getChildList().add(new ManyTreeNode(child));
				break;
			} else {
				if (item.getChildList() != null && item.getChildList().size() > 0) {
					addChild(item, child);
				}
			}
		}

	}

	// 遍历多叉树
	public String iteratorTree(ManyTreeNode manyTreeNode) {
		List<String> rsData = new ArrayList<>();
		StringBuilder buffer = new StringBuilder();
		buffer.append("|");
		if (manyTreeNode != null) {
			for (ManyTreeNode index : manyTreeNode.getChildList()) {
				buffer.append(index.getData().getNodeId() + ",");
				if (index.getChildList() != null && index.getChildList().size() > 0) {
					buffer.append(iteratorTree(index));
					
				}
			}
		}
		
		//buffer.append("\n");
		
		return buffer.toString();
		
		
	}

	public ManyTreeNode getRoot() {
		return root;
	}

	public void setRoot(ManyTreeNode root) {
		this.root = root;
	}

	public static void main(String[] args) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		
		treeNodes.add(new TreeNode("系统权限管理", "root"));

		treeNodes.add(new TreeNode("用户管理", "系统权限管理"));
		treeNodes.add(new TreeNode("角色管理", "系统权限管理"));
		treeNodes.add(new TreeNode("组管理", "系统权限管理"));
		treeNodes.add(new TreeNode("用户菜单管理", "系统权限管理"));
		treeNodes.add(new TreeNode("角色菜单管理", "系统权限管理"));
		treeNodes.add(new TreeNode("用户权限管理", "系统权限管理"));

		treeNodes.add(new TreeNode("站内信", "root"));
		treeNodes.add(new TreeNode("写信", "站内信"));
		treeNodes.add(new TreeNode("收信", "站内信"));
		treeNodes.add(new TreeNode("草稿", "站内信"));

        treeNodes.add(new TreeNode("站内信rp", "root"));
        treeNodes.add(new TreeNode("站内信lp", "root"));

        // 站内信有3个父亲
        treeNodes.add(new TreeNode("站内信", "站内信rp"));
        treeNodes.add(new TreeNode("站内信", "站内信lp"));

		ManyNodeTree tree = new ManyNodeTree();

		System.out.println(tree.iteratorTree(tree.createTree(treeNodes).getRoot()));
		System.out.println("-------------");
		String result = tree.iteratorTree(tree.createTree(treeNodes).getRoot());
		
	}

}
