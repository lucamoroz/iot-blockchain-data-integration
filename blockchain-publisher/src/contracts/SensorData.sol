pragma solidity >=0.4.22;
pragma experimental ABIEncoderV2;

contract SensorData {
   
    struct SensorDataItem {
        string temperature;         // 16.67
        string icon;                // clear-night
//        string humidity;            // 0.53
        //string visibility;          // 10.0
        //string summary;             // Clear
        //string apparentTemperature; // 3.95
        //string pressure;            // 1022.69
        //string windSpeed;           // 11.23
        //string cloudCover;          // 0.0
        //string time;                // 1388552400
        //string windBearing;         // 271.0
        //string precipIntensity;     // 0.0
        //string dewPoint;            // 2.41
        //string precipProbability;   // 0.0
    }
    
    SensorDataItem[] public items;
   
    event Notification(string message);
   
    function addDataItem(string memory temperature, string memory icon) public {
        items.push(SensorDataItem(temperature, icon));
        emit Notification('New data item!');
    }
    
    function getLatestDataItem() public view returns (string memory, string memory) {
        if(items.length > 0){
            uint idx = items.length - 1;
            return (items[idx].temperature, 
                    items[idx].icon);
        }
    }
    
    function getDataItem(uint32 itemId) public view returns (SensorDataItem memory){
        return items[itemId];
    }
    
    function getAllDataItems() public view returns (SensorDataItem[] memory){
        return items;
    }
}
