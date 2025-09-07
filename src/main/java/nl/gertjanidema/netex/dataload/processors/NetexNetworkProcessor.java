package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Network;

import nl.gertjanidema.netex.dataload.dto.StNetexNetwork;

public class NetexNetworkProcessor extends AbstractNetexProcessor {
 
    public static StNetexNetwork process(Network network) throws Exception {
        var netexNetwork = new StNetexNetwork();
        netexNetwork.setId(network.getId());
        var validBetween = network.getValidBetween();
        if (validBetween != null && validBetween.size() > 0) {
            netexNetwork.setFromDate(network.getValidBetween().get(0).getFromDate());
            netexNetwork.setToDate(network.getValidBetween().get(0).getToDate());
        }
        netexNetwork.setName(toString(network.getName()));
        netexNetwork.setShortName(toString(network.getShortName()));
        netexNetwork.setDescription(toString(network.getDescription()));
        netexNetwork.setGroupOfLinesType(network.getGroupOfLinesType().toString());
//        netexNetwork.setAuthorityRef(network.);
        return netexNetwork;
    }
}