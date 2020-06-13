package it.polito.dp2.BIB.sol3.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.BIB.sol3.service.BadRequestServiceException;
import it.polito.dp2.BIB.sol3.service.BookshelvesService;
import it.polito.dp2.BIB.sol3.service.ConflictServiceException;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelves;
import it.polito.dp2.BIB.sol3.service.jaxb.Item;
import it.polito.dp2.BIB.sol3.service.jaxb.Items;
import it.polito.dp2.BIB.sol3.service.jaxb.Ownership;
import it.polito.dp2.BIB.sol3.service.jaxb.Ownerships;
import java.math.BigInteger;
import java.net.URI;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/biblio")
@Api(value = "/biblio")
public class BookshelvesResources {
  public UriInfo uriInfo;
  BookshelvesService service;

  public BookshelvesResources(@Context UriInfo uriInfo) {
    this.uriInfo = uriInfo;
    this.service = new BookshelvesService(uriInfo);
  }

  @GET
  @Path("bookshelves")
  @ApiOperation(value = "getBookshelves", notes = "read bookshelves resource")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Bookshelves.class),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Bookshelves getBookshelves(
    @ApiParam("The keyword to be used for the search") @QueryParam(
      "keyword"
    ) @DefaultValue("") String keyword
  ) {
    try {
      return this.service.getBookshelves(keyword);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new InternalServerErrorException(e);
    }
  }

  @POST
  @Path("bookshelves")
  @ApiOperation(
    value = "createBookshelf",
    notes = "create a new bookshelf",
    response = Bookshelf.class
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 201, message = "Created", response = Bookshelf.class),
      @ApiResponse(code = 400, message = "Bad Request"),
    }
  )
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Response createBookshelf(Bookshelf b) {
    try {
      Bookshelf rb = this.service.createBookshelf(b);
      return Response.created(new URI(rb.getSelf())).entity(rb).build();
    } catch (Exception e1) {
      throw new InternalServerErrorException();
    }
  }

  @GET
  @Path("bookshelves/{id}")
  @ApiOperation(value = "getBookshelf", notes = "read a single bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Bookshelf.class),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Bookshelf getBookshelf(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id
  ) {
    Bookshelf b;
    try {
      b = service.getBookshelf(id);
    } catch (Exception e) {
      throw new InternalServerErrorException();
    }
    if (b == null) throw new NotFoundException();

    return b;
  }

  @PUT
  @Path("bookshelves/{id}")
  @ApiOperation(value = "updateBookshelf", notes = "update a single bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Bookshelf.class),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Bookshelf updateBookshelf(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id,
    Bookshelf nb
  ) {
    Bookshelf b;
    try {
      b = service.updateBookshelf(id, nb);
    } catch (Exception e) {
      throw new InternalServerErrorException();
    }
    if (b == null) throw new NotFoundException();

    return b;
  }

  @PUT
  @Path("bookshelves/{id}/size")
  @ApiOperation(value = "setSize", notes = "update the size of a bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Integer.class),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Consumes({ MediaType.TEXT_PLAIN })
  @Produces({ MediaType.TEXT_PLAIN })
  public Integer setSize(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id,
    Integer size
  ) {
    Integer s;
    try {
      s = this.service.setSize(id, size);
    } catch (ConflictServiceException e) {
      throw new ClientErrorException(Response.Status.CONFLICT);
    } catch (Exception e) {
      throw new InternalServerErrorException();
    }
    if (s == null) throw new NotFoundException();

    return s;
  }

  @GET
  @Path("bookshelves/{id}/size")
  @ApiOperation(value = "getSize", notes = "update a single bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Integer.class),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Consumes({ MediaType.TEXT_PLAIN })
  @Produces({ MediaType.TEXT_PLAIN })
  public Integer getSize(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id
  ) {
    Integer s;
    try {
      s = this.service.getSize(id);
      System.out.println(s);
    } catch (Exception e) {
      throw new InternalServerErrorException();
    }
    if (s == null) throw new NotFoundException();

    return s;
  }

  @DELETE
  @Path("bookshelves/{id}")
  @ApiOperation(value = "deleteBookshelf", notes = "read a single bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 204,
        message = "No content",
        response = Bookshelf.class
      ),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void deleteBokshelf(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id
  ) {
    Bookshelf b;
    try {
      b = service.deleteBookshelf(id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalServerErrorException();
    }

    if (b == null) {
      throw new NotFoundException();
    }
  }

  @GET
  @Path("bookshelves/{id}/ownerships/{idi}")
  @ApiOperation(value = "getOwnership", notes = "read elements resource")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Ownership.class),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Ownership getOwnership(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id,
    @ApiParam("The id of the ownership") @PathParam("idi") BigInteger idi
  ) {
    try {
      Ownership o = this.service.getOwnership(id, idi);
      if (o == null) {
        throw new NotFoundException();
      }
      return o;
    } catch (NotFoundException e1) {
      throw e1;
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @PUT
  @Path("bookshelves/{id}/ownerships/{idi}")
  @ApiOperation(
    value = "createOwnership",
    notes = "Create a new ownership for an existing bookshelf"
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "Created", response = Ownership.class),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 409, message = "Conflict"),
    }
  )
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Ownership createOwnership(
    @ApiParam("The id of the bookshelf") @PathParam(
      "id"
    ) BigInteger bookshelfId,
    @ApiParam("The id of the item") @PathParam("idi") BigInteger itemId,
    Ownership ownership
  ) {
    Ownership o;
    try {
      o = this.service.createOwnership(bookshelfId, itemId, ownership);
      if (o == null) {
        throw new NotFoundException();
      }
      return ownership;
    } catch (NotFoundException e) {
      throw e;
    } catch (ConflictServiceException e) {
      throw new ClientErrorException(Response.Status.CONFLICT);
    } catch (BadRequestServiceException e) {
      throw new BadRequestException();
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @DELETE
  @Path("bookshelves/{id}/ownerships/{idi}")
  @ApiOperation(value = "deleteOwnership", notes = "read a single bookshelf")
  @ApiResponses(
    value = {
      @ApiResponse(code = 204, message = "No content"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 409, message = "Conflict"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void deleteOwnership(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger bid,
    @ApiParam("The id of the item") @PathParam("idi") BigInteger idi
  ) {
    Ownership o;
    try {
      o = service.deleteOwnership(bid, idi);
    } catch (ConflictServiceException e) {
      throw new ClientErrorException(Response.Status.CONFLICT);
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalServerErrorException();
    }

    if (o == null) {
      throw new NotFoundException();
    }
  }

  @GET
  @Path("bookshelves/{id}/ownerships")
  @ApiOperation(value = "getOwnerships", notes = "read ownerships resource")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Ownerships.class),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Ownerships getOwnerships(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id,
    @ApiParam("The id of the ownership") @PathParam("idi") BigInteger idi
  ) {
    try {
      Ownerships e = this.service.getOwnerships(id);
      if (e == null) {
        throw new NotFoundException();
      }
      return e;
    } catch (NotFoundException e1) {
      throw e1;
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @GET
  @Path("bookshelves/{id}/ownerships/targets")
  @ApiOperation(value = "getTargets", notes = "read ownerships resource")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Items.class),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Items getTargets(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id
  ) {
    try {
      Items i = this.service.getTargets(id);
      if (i == null) {
        throw new NotFoundException();
      }
      return i;
    } catch (NotFoundException e1) {
      throw e1;
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @GET
  @Path("bookshelves/{id}/ownerships/targets/{idi}")
  @ApiOperation(value = "getTarget", notes = "read ownerships resource")
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = Item.class),
      @ApiResponse(code = 404, message = "Not Found"),
    }
  )
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Item getTarget(
    @ApiParam("The id of the bookshelf") @PathParam("id") BigInteger id,
    @ApiParam("The id of the item") @PathParam("idi") BigInteger idi
  ) {
    try {
      Item i = this.service.getTarget(id, idi);
      if (i == null) {
        throw new NotFoundException();
      }
      return i;
    } catch (NotFoundException e1) {
      throw e1;
    } catch (Exception e1) {
      e1.printStackTrace();
      throw new InternalServerErrorException();
    }
  }
}
