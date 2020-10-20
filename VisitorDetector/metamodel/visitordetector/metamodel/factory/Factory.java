package visitordetector.metamodel.factory;

import ro.lrg.xcore.metametamodel.XEntity;
import visitordetector.metamodel.entity.*;
import visitordetector.metamodel.impl.*;

public class Factory {
   protected static Factory singleInstance = new Factory();
   public static Factory getInstance() { return singleInstance;}
   protected Factory() {}
   private LRUCache<Object, XEntity> lruCache_ = new LRUCache<>(1000);
   public void setCacheCapacity(int capacity) {
       lruCache_.setCapacity(capacity);
   }
   public void clearCache() {lruCache_.clear();}
   public MClass createMClass(org.eclipse.jdt.core.IType obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MClassImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MClass)instance;
    }
   public MSystem createMSystem(org.eclipse.jdt.core.IJavaProject obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MSystemImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MSystem)instance;
    }
}
