import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Blog e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Blogs', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Blog').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Blog page', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('blog');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Blog page', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Blog');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Blog page', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Blog');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Blog', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Blog');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-11T00:28').invoke('val').should('equal', '2021-05-11T00:28');


    cy.get(`[data-cy="title"]`).type('Sleek Chief', { force: true }).invoke('val').should('match', new RegExp('Sleek Chief'));


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

    cy.setFieldSelectToLastOfEntity('appuser');

    cy.setFieldSelectToLastOfEntity('community');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/blogs*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Blog', () => {
    cy.intercept('GET', '/api/blogs*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/blogs/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('blog');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('blog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/blogs*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('blog');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
