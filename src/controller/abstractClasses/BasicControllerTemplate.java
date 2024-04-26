package controller.abstractClasses;

import model.BasicMap;

public abstract class BasicControllerTemplate {

	private BasicMap completeBasicMap;

	public BasicControllerTemplate(int[][] baseData, int[][] valueData) {
		initCompleteBasicMap(baseData, valueData);
	}

	public abstract void initCompleteBasicMap(int[][] baseData, int[][] valueData);

	public BasicMap getCompleteBasicMap() {
		return completeBasicMap;
	}

	public void setCompleteBasicMap(BasicMap completeBasicMap) {
		this.completeBasicMap = completeBasicMap;
	}

}
