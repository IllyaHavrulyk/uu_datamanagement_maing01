<?xml version='1.0' encoding='UTF-8'?>
<GSKDocument xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <DocumentIdentification v="${gskDocument.documentIdentification}"/>
  <DocumentVersion v="1"/>
  <DocumentType v="${metadata.documentType}"/>
  <SenderIdentification v="${metadata.sender}"/>
  <ReceiverIdentification v="${metadata.receiver}"/>
  <CreationDateTime v="${metadata.creationDateTime}"/>
  <GSKTimeInterval v="${metadata.timeInterval}"/>
  <Domain v="${metadata.domain}"/>
  <#if gskDocument.gskSeries?has_content>
  <#list gskDocument.gskSeries as seria>
  <GSKSeries>
    <TimeSeriesIdentification v="1"/>
    <BusinessType v="${seria.businessType}"/>
    <Area v="${seria.area}"/>
    <#if seria.manualGSKBlock?has_content>
    <#list seria.manualGSKBlock as manualBlock>
    <ManualGSK_Block>
      <GSK_Name v="${manualBlock.gskName}"/>
      <TimeInterval v="${manualBlock.timeInterval}"/>
      <#if manualBlock.manualNodes?has_content>
      <#list manualBlock.manualNodes as manualNode>
      <ManualNodes>
        <NodeName v="${manualNode.nodeName}"/>
        <Factor v="${manualNode.factor}"/>
      </ManualNodes>
      </#list>
      </#if>
    </ManualGSK_Block>
    </#list>
    </#if>
    <#if seria.countryGSKBlock?has_content>
    <#list seria.countryGSKBlock as countryBlock>
    <CountryGSK_Block>
      <GSK_Name v="${countryBlock.gskName}"/>
      <TimeInterval v="${countryBlock.timeInterval}"/>
      <#if countryBlock.countryNodes?has_content>
      <#list countryBlock.countryNodes as countryNode>
      <CountryNodes>
        <NodeName v="${countryNode.nodeName}"/>
      </CountryNodes>
      </#list>
      </#if>
    </CountryGSK_Block>
    </#list>
    </#if>
    <#if seria.autoGSKBlocks?has_content>
    <#list seria.autoGSKBlocks as autoBlock>
    <AutoGSK_Block>
      <GSK_Name v="${autoBlock.gskName}"/>
      <TimeInterval v="${autoBlock.timeInterval}"/>
      <#if autoBlock.autoNodes?has_content>
      <#list autoBlock.autoNodes as autoNode>
        <AutoNodes>
          <NodeName v="${autoNode.nodeName}"/>
        </AutoNodes>
      </#list>
      </#if>
    </AutoGSK_Block>
    </#list>
    </#if>
    </GSKSeries>
  </#list>
  </#if>
</GSKDocument>
