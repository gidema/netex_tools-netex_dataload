package nl.gertjanidema.netex.dataload.processors;

import org.rutebanken.netex.model.Network;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import nl.gertjanidema.netex.dataload.dto.StNetexNetwork;

@Component
public class StNetexNetworkProcessor extends AbstractItemProcessor implements ItemProcessor<Network, StNetexNetwork> {
 
    @Override
    public StNetexNetwork process(Network network) throws Exception {
        var netexNetwork = new StNetexNetwork();
        netexNetwork.setNetexId(network.getId());
        netexNetwork.setVersion(network.getVersion());
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