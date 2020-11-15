pragma solidity >=0.4.22;

contract SensorData {
    
    bytes[] public items;
   
    event Notification(string message, uint index);
   
    function addDataItem(bytes memory data) public {
        items.push(data);
        emit Notification('New data item!', items.length - 1);
    }
    
    function getLatestDataItem() public view returns (bytes memory) {
        if(items.length > 0){
            uint idx = items.length - 1;
            return items[idx];
        }
    }
    
    function getDataItem(uint32 itemId) public view returns (bytes memory){
        return items[itemId];
    }
}
