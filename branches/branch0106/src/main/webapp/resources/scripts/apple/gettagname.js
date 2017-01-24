function getNetuiTagName(id) {
   return netui_names[id];
}

// method which will return a real id for a tagId,
// the tag parameter will be used to find the scopeId for
// containers that may scope their ids
function getNetuiTagName(id, tag)
{
   var scopeId = getScopeId(tag);
   if (scopeId == "")
      return netui_names[id];
   else
      return netui_names[scopeId  + "__" + id];
}

// method which get a tag will find any scopeId that,
// was inserted by the containers
function getScopeId(tag)
{
   if (tag == null)
      return "";
   if (tag.scopeId != null)
      return tag.scopeId;
   return getScopeId(tag.parentElement);
}
