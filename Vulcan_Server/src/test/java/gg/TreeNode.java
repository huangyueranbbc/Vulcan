package gg;

public class TreeNode {

	/** 节点Id */
	private String nodeId;
	/** 父节点Id */
	private String parentId;
	/** 文本内容 */
	private String text;

	/**
	 * 构造函数
	 * 
	 * @param nodeId
	 *            节点Id
	 */
	public TreeNode(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 构造函数
	 * 
	 * @param nodeId
	 *            节点Id
	 * @param parentId
	 *            父节点Id
	 */
	public TreeNode(String nodeId, String parentId) {
		this.nodeId = nodeId;
		this.parentId = parentId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
