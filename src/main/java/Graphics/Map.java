package Graphics;
import Tools.LayerManagerLayer;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.layers.Earth.OSMCycleMapLayer;
import gov.nasa.worldwind.layers.Earth.OSMMapnikLayer;
import gov.nasa.worldwind.terrain.BasicElevationModel;


public class Map extends WorldWindowGLCanvas {


  public Map(){
    super();
    this.setPreferredSize(new java.awt.Dimension(1000, 800));
    this.setModel(new BasicModel());
    LayerList layers = this.getModel().getLayers();
    for(Layer l : layers){
      System.out.println(l.getName());

      if(l.getName().contains( "Stars")){
       layers.remove(l);
      }

      if(l.getName().contains( "Atmosphere")){
        layers.remove(l);
      }

      if(l.getName().contains( "Blue Marble May 2004")){
        layers.remove(l);
      }

      if(l.getName().contains( "i-cubed Landsat")){
        layers.remove(l);
      }

      if(l.getName().contains( "USGS NAIP Plus")){
        layers.remove(l);
      }

      if(l.getName().contains( "Bing Imagery")){
        layers.remove(l);
      }

      if(l.getName().contains( "USGS Topo Base Map")){
        layers.remove(l);
      }
      if(l.getName().contains( "USGS Topo Base Map Large Scale")){
        layers.remove(l);
      }
      if(l.getName().contains( "USGS Topo Scanned Maps 1:250K")){
        layers.remove(l);
      }
      if(l.getName().contains( "USGS Topo Scanned Maps 1:100K")){
        layers.remove(l);
      }
      if(l.getName().contains( "USGS Topo Scanned Maps 1:24K")){
        layers.remove(l);
      }
      if(l.getName().contains( "Political Boundaries")){
        layers.remove(l);
      }
      if(l.getName().contains( "Earth at Night")){
        layers.remove(l);
      }
      if(l.getName().contains( "Place Names")) {
        layers.remove(l);
      }
    }
    this.getModel().getLayers().add(new OSMMapnikLayer());
    layers.add(new LayerManagerLayer(this));
  }

  public void update() {

  }
}
